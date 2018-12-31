//just for 2019
var app = new Vue({
  el: '#app',
  data: {
    tasks: [],
    recommendTasks: [],
    //分页
    currentPage: 0,
    total: 0,
    menuIndex:'1-1',
    status: 2,
    requestContent:'registerTask',
  },
  methods: {
      menuSelect(index,indexPath){
          console.log(indexPath);
          this.menuIndex = indexPath[1]?indexPath[1]:indexPath[0];
          switch (this.menuIndex) {
              case '1-1':
                  this.status = 2;
                  this.requestContent='registerTask';
                  break;
              case '1-2':
                  this.status = 3;
                  this.requestContent='registerTask';
                  break;
              case '1-3':
                  this.status = 0;
                  this.requestContent='registerTask';
                  break;
              case '2-1':
                  this.status = 2;
                  this.requestContent='publishedTask';
                  break;
              case '2-2':
                  this.status = 1;
                  this.requestContent='publishedTask';
                  break;
              case '2-3':
                  this.status = 3;
                  this.requestContent='publishedTask';
                  break;
              case '3-1':
                  //TODO
                  break;
          }
          this.getTasksList();
      },
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
                 url: "http://"+login.ip+"/api/user/"+login.username+"/"+this.requestContent,
                 params:{
                    status: this.status,
                 },
                 headers: {
                     'username': login.username,
                     'token': login.token
                 }
               })
               .then(function (response) {
                     console.log(response);
                     that.tasks = response.data;
                     for(let t of  that.tasks) {
                         t.properties = JSON.parse(t.properties);
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