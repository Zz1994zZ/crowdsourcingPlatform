var app = new Vue({
  el: '#app',
  data: {
    //表单
    task:{
      name:'',
      type:'',
      properties:{
          expStartTime:'',
          expEndTime:'',
          crowdNum: 1,
          description:'',
          price:0
      }
    },
  },
  methods: {
      onPost(){
          let that = this;
          this.task.properties.description = editor.txt.html();
          axios(
              {
                  method: 'post',
                  url: "http://"+login.ip+"/api/task",
                  headers: {
                      'username': login.username,
                      'token': login.token
                  },
                  data: {
                      name:this.task.name,
                      type:this.task.type,
                      properties:JSON.stringify(this.task.properties)
                  }
              })
              .then(function (response) {
                  console.log(response);
                  let success = response.data.success;
                  if(success){
                      that.$message({
                          message: '发布成功！',
                          type: 'success'
                      });
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
  },
  mounted: function () {

  }
})