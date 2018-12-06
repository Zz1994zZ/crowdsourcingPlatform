function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}
var app = new Vue({
  el: '#app',
  data: {
    id:'',
    task: {},
    registers:[]
  },
  methods: {
      getTaskInfo(){
         let that = this;
              axios(
              {
                method: 'get',
                url: "http://"+login.ip+"/api/task/"+that.id,
                headers: {
                    'username': login.username,
                    'token': login.token
                }
              })
              .then(function (response) {
                    console.log(response);
                    let data = response.data;
                    that.task = data.task;
                    that.registers = data.registers;
                    that.task.properties = JSON.parse(that.task.properties);
              })
              .catch(function (error) {
                console.log(error);
                that.$message({
                      message: '网络错误！',
                      type: 'error'
                });
              });
      },
      confirm(){
              let that = this;
              this.$confirm('申请成功后不可以轻言放弃哟, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  axios(
                      {
                          method: 'post',
                          url: "http://"+login.ip+"/api/task/"+that.id+'/user',
                  headers: {
                      'username': login.username,
                      'token': login.token
                  }
              })
              .then(function (response) {
                      console.log(response);
                      let data = response.data;
                      if(data.success){
                          that.$message({
                              type: 'success',
                              message: data.message
                          });
                      }else{
                          that.$message({
                              message: data.message,
                              type: 'error'
                          });
                      }
                      that.getTaskInfo();
                  })
              .catch(function (error) {
                  console.log(error);
                  that.$message({
                      message: '网络错误！',
                      type: 'error'
                  });
              });
              }).catch(() => {
                     that.$message({
                      type: 'info',
                      message: '取消报名'
                  });
              });
      }
  },
  mounted: function () {
      this.id=getQueryVariable('id');
    login.onFinishLogin=this.getTaskInfo;
      if(login.username&&login.token){
          this.getTaskInfo();
      }
  }
})