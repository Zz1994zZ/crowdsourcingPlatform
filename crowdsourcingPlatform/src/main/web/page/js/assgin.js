var app = new Vue({
  el: '#app',
  data: {
      tasks:[
             {
                 "id":1,
                  "g":1,
                  "skill":"java",
                  "alpha":0.5,
                  "registers":[1,2,3,4],
                  "models":[
                     {"id":1,"complexity":0.6},
                     {"id":1,"complexity":0.7}
                  ]
             },
            {
                "id":2,
                 "g":2,
                 "skill":"java",
                 "alpha":0.5,
                 "registers":[2,4,6,8],
                 "models":[
                    {"id":1,"complexity":0.6},
                    {"id":1,"complexity":0.7}
                 ]
            }
          ],
      users:[
             {"id":1,"workTime":1,"skillMap":{"java":0.1,"php":0.5}},
             {"id":2,"workTime":1,"skillMap":{"java":0.2,"php":0.5}},
             {"id":3,"workTime":1,"skillMap":{"java":0.3,"php":0.5}},
             {"id":4,"workTime":1,"skillMap":{"java":0.4,"php":0.5}},
             {"id":5,"workTime":1,"skillMap":{"java":0.5,"php":0.5}},
             {"id":6,"workTime":1,"skillMap":{"java":0.6,"php":0.5}},
             {"id":7,"workTime":1,"skillMap":{"java":0.7,"php":0.5}},
             {"id":8,"workTime":1,"skillMap":{"java":0.8,"php":0.5}},
             {"id":9,"workTime":1,"skillMap":{"java":0.9,"php":0.5}},
             {"id":10,"workTime":1,"skillMap":{"java":12,"php":0.5}}
          ],
      type:1
  },
  methods: {
        post(){
                let that = this;
                axios(
                          {
                              method: 'post',
                              url: "http://"+login.ip+"/api/massign",
                              headers: {
                                  'username': login.username,
                                  'token': login.token
                              },
                              data: {
                                 tasks:that.tasks,
                                 users:that.users,
                                 type:that.type
                              }
                          })
                          .then(function (response) {

                          })
                          .catch(function (error) {
                              console.log(error);
                              that.$message({
                                  message: '网络错误！',
                                  type: 'error'
                              });
                          });



        }
  },
    mounted: function () {

    }
})