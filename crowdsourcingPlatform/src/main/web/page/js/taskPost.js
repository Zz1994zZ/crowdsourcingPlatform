var app = new Vue({
  el: '#app',
  data: {
    ip:'localhost:8080',
    dialogFormVisible: false,
    username: '',
    password: '',
    loginLabel: '登陆',

    //表单
    form: {
      name: '',
      region: '',
      date1: '',
      date2: '',
      delivery: false,
      type: [],
      resource: '',
      desc: ''
    },

    //登陆授权token
    token:'',
    loginData:'',
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
          }

      });
  }
})