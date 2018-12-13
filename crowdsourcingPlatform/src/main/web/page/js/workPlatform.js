var app = new Vue({
  el: '#app',
  data: {
    tasks: [],
    recommendTasks: [],
    //分页
    currentPage: 0,
    total: 0,
  },
  methods: {
      handleOpen(key, keyPath) {
          console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
          console.log(key, keyPath);
      },
      getTasksList(){
          let that = this;
               axios(
               {
                 method: 'get',
                 url: "http://"+login.ip+"/api/user/"+login.username+"/registerTask",
                 headers: {
                     'username': login.username,
                     'token': login.token
                 }
               })
               .then(function (response) {
                     console.log(response);
                     that.tasks = response.data;
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
    login.onFinishLogin=this.getTasksList;
      if(login.username&&login.token){
          this.getTasksList();
      }
  }
})