var app = new Vue({
  el: '#app',
  data: {
      newUser:false,
      editing:false,
      options:[1,2,3,4,5,6,7,8,9,10],
    //表单
    userInfo:{
      name:'',
      skillList:{},
      info:{
          sex:'',
          workYears:'',
          company: '',
          description:'',
          qq:'',
          wechat:'',
      },
      extention:{
          activeTime:[]
      }
    },
  },
  methods: {
      onPost(){
          let that = this;
          this.userInfo.info.description = editor.txt.html();
          axios(
              {
                  method: this.newUser? 'post':'put',
                  url: "http://"+login.ip+"/api/userInfo",
                  headers: {
                      'username': login.username,
                      'token': login.token
                  },
                  data: {
                      username: login.username,
                      name:this.userInfo.name,
                      skillList:JSON.stringify(this.userInfo.skillList),
                      info:JSON.stringify(this.userInfo.info),
                      extention:JSON.stringify(this.userInfo.extention),
                  }
              })
              .then(function (response) {
                  console.log(response);
                  let success = response.data.success;
                  if(success){
                      that.$message({
                          message: '编辑成功！',
                          type: 'success'
                      });
                      window.location.href="userInfo.html";
                  }else{
                      that.$message({
                          message: response.data.message,
                          type: 'error'
                      });
                  }
              })
              .catch(function (error) {
                  console.log(error);
                  that.$message({
                      message: '网络错误！',
                      type: 'error'
                  });
              });
      },
      getUserInfo(){
          let that = this;
          axios(
              {
                  method: 'get',
                  url: "http://"+login.ip+"/api/userInfo/"+login.username,
                  headers: {
                      'username': login.username,
                      'token': login.token
                  }
              })
              .then(function (response) {
                  console.log(response);
                  let info = response.data;
                  info.info =  JSON.parse(info.info);
                  info.extention =  JSON.parse(info.extention);
                  info.skillList =  JSON.parse(info.skillList);
                  that.userInfo = info;
                  editor.txt.html(that.userInfo.info.description);
              })
              .catch(function (error) {
                  console.log(error);
                  that.newUser = true;
                  that.$message({
                      message: '网络错误！',
                      type: 'error'
                  });
              });
      },
  },
  mounted: function () {
      this.getUserInfo();
  }
})