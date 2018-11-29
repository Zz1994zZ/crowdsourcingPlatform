var app = new Vue({
  el: '#app',
  data: {
    ip:'localhost:8080',
    dialogFormVisible: false,
    username: '',
    password: '',
    loginLabel: '登陆',

    tasks: [],

    //登陆授权token
    token:'',
    loginData:'',
    //分页
    currentPage: 1,
    total: 100
  },
  methods: {
      onSubmit() {
      let that = this;
      if(!$.trim(this.username)||!$.trim(this.password)){
        return;
      }
      axios.post('http://localhost:8080/api/token', {
        username: this.username,
        password: this.password
      })
      .then(function (response) {
        console.log(response);
          let data = response.data;
          let msg = data.message;
          let type = data.success? 'success':'error';
          that.loginData = data.data;
          if(that.loginData){
              that.token = that.loginData.tokenCode;
              sessionStorage.setItem('token',that.token);
              sessionStorage.setItem('username',that.username);
              that.dialogFormVisible = false;
          }
          that.$message({
              message: msg,
              type: type
          });
      })
      .catch(function (error) {
        console.log(error);
        that.$message({
              message: '网络错误！',
              type: 'error'
          });
      });
     },
      onLogout() {
          this.token = '';
          this.username = '';
          sessionStorage['token'] = '';
          sessionStorage['username'] = '';
      },
      getTasksList(){
         let that = this;
              axios(
              {
                method: 'get',
                url: "http://"+that.ip+"/api/task",
                headers: {
                    'username': that.username,
                    'token': that.token
                },
                params : { //请求参数
                    page : that.currentPage,
                    per_page : 10
                },
                data: {
                    body: that.username
                }
              })
              .then(function (response) {
                    console.log(response);
                    let data = response.data;
                    that.tasks = data.tasks;
                    that.total = data.count;
              })
              .catch(function (error) {
                console.log(error);
                that.$message({
                      message: '网络错误！',
                      type: 'error'
                });
              });
      },
      handleSizeChange(val) {
          console.log(`每页 ${val} 条`);
      },
      handleCurrentChange(val) {
          console.log(`当前页: ${val}`);
          this.getTasksList();
      }
  },
  mounted: function () {
    let that = this;
    this.$nextTick(function () {
        let username = sessionStorage['username'];
        let token = sessionStorage['token'];
        if(username&&token){
            that.username = username;
            that.token = token;
            that.getTasksList();
        }else{
            that.dialogFormVisible = true;
        }
    })
  }
})