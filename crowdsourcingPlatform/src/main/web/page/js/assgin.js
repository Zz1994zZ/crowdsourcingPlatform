var app = new Vue({
  el: '#app',
  data: {
      // tasks:[
      //        {
      //            "id":1,
      //             "g":1,
      //             "skill":"java",
      //             "alpha":0.5,
      //             "registers":[1,2,3,4],
      //             "models":[
      //                {"id":1,"complexity":0.6},
      //                {"id":1,"complexity":0.7}
      //             ]
      //        },
      //       {
      //           "id":2,
      //            "g":2,
      //            "skill":"java",
      //            "alpha":0.5,
      //            "registers":[2,4,6,8],
      //            "models":[
      //               {"id":1,"complexity":0.6},
      //               {"id":1,"complexity":0.7}
      //            ]
      //       }
      //     ],
      // users:[
      //        {"id":1,"nickname":"user1","workTime":1,"skillMap":{"java":0.1,"php":0.5}},
      //        {"id":2,"nickname":"user2","workTime":1,"skillMap":{"java":0.2,"php":0.5}},
      //        {"id":3,"nickname":"user3","workTime":1,"skillMap":{"java":0.3,"php":0.5}},
      //        {"id":4,"nickname":"user4","workTime":1,"skillMap":{"java":0.4,"php":0.5}},
      //        {"id":5,"nickname":"user5","workTime":1,"skillMap":{"java":0.5,"php":0.5}},
      //        {"id":6,"nickname":"user6","workTime":1,"skillMap":{"java":0.6,"php":0.5}},
      //        {"id":7,"nickname":"user7","workTime":1,"skillMap":{"java":0.7,"php":0.5}},
      //        {"id":8,"nickname":"user8","workTime":1,"skillMap":{"java":0.8,"php":0.5}},
      //        {"id":9,"nickname":"user9","workTime":1,"skillMap":{"java":0.9,"php":0.5}},
      //        {"id":10,"nickname":"user10","workTime":1,"skillMap":{"java":12,"php":0.5}}
      //     ],
      type:1,
      //单任务时用到
      models:[
          {
              id:0,
              complexity:0.7
          },
          {
              id:1,
              complexity:0.6
          },
          {
              id:2,
              complexity:0.6
          },
      ],
      //添加模块弹框
      dialogFormVisible:false,
      //任务配置弹框
      taskDialogFormVisible: false,
      //添加工人弹框
      workerDialogFormVisible: false,
      //三个弹框绑定对象
      model:{},
      worker:  {"id":1,"nickname":"nickname","workTime":1,"skillMap":'{"java":0.0,"php":0.0}'},
      // task:{ "id":0,
      //     "g":0,
      //     "skill":"空",
      //     "alpha":0
      // },
      task:{
          id:0,
          skill:"java",
          g:3,
          alpha:0.8,
      },
      //被选中的已录入工人
      selectedKeys:[],
      //左边工人数据
      userData: [
          {
              id:0,
              nickname:"d1",
              skillMap:{java:0.8},
              workTime:62400
          },
          {
              id:1,
              nickname:"d2",
              skillMap:{java:0.7},
              workTime:960
          },
          {
              id:2,
              nickname:"d3",
              skillMap:{java:0.6},
              workTime:61440
          },
          {
              id:3,
              nickname:"d4",
              skillMap:{java:0.5},
              workTime:496
          },
          {
              id:4,
              nickname:"d5",
              skillMap:{java:0.5},
              workTime:16515073
          },
          {
              id:5,
              nickname:"d6",
              skillMap:{java:0.5},
              workTime:14680064
          },
          {
              id:6,
              nickname:"d7",
              skillMap:{java:0.5},
              workTime:16777215
          },
          {
              id:7,
              nickname:"d8",
              skillMap:{java:0.4},
              workTime:60
          },
          {
              id:8,
              nickname:"d9",
              skillMap:{java:0.3},
              workTime:253952
          },
          {
              id:9,
              nickname:"d10",
              skillMap:{java:0.2},
              workTime:16128
          }
      ],
      //右边数据（分配时提交的）
      selectedUsers: [],
      assignResult:{models:[]}
  },
  methods: {
      addWorker(){
          let w = {"id":1,"nickname":"nickname","workTime":1,"skillMap":{"java":0.0,"php":0.0}};
          w.id = parseInt(this.worker.id);
          w.nickname = this.worker.nickname;
          w.workTime = this.worker.workTime;
          w.skillMap = JSON.parse(this.worker.skillMap);
          this.userData.push(w);
        this.workerDialogFormVisible = false;
      },
      addModel(){
          this.models.push(this.model);
          this.model = {};
          this.dialogFormVisible = false;
      },
      //删除被选中的工人
      delWokers(){
          this.userData = this.userData.filter(item => { return this.selectedKeys.every(data => data!== item.id) });
      },
      //任务列表相关
      handleEdit(index, row) {
          console.log(index, row);
      },
      handleDelete(index, row) {
          console.log(index, row);
          // let i = 0;
          // for(;i<this.models.length;i++){
          //     if(this.models[i].id == row.id)
          //         break;
          // }
          this.models.splice(index,1)
      },
      handleUserEdit(index, row) {
          console.log(index, row);
      },
      handleUserDelete(index, row) {
          console.log(index, row);
          this.userData.splice(index,1)
      },
        post(){
                let that = this;
                //模块拼进task
                this.task["models"] = this.models;
                this.task["registers"] = this.selectedUsers;
                let tasks = [];
                tasks.push(this.task);
                let users = this.userData;
            axios(
                          {
                              method: 'post',
                              url: "http://"+login.ip+"/api/sassign",
                              headers: {
                                  'username': login.username,
                                  'token': login.token
                              },
                              data: {
                                 tasks:tasks,
                                 users:users,
                                 type:that.type
                              }
                          })
                          .then(function (response) {
                              that.assignResult = response.data;
                          })
                          .catch(function (error) {
                              console.log(error);
                              that.$message({
                                  message: '网络错误！',
                                  type: 'error'
                              });
                          });
        },
      handleCheckChange(checkedKeys){
          this.selectedKeys = checkedKeys;
      },
      getTime(activeTime){
          let r = [];
          let index = 1;
          for(let i = 24;i>=1;i--){
              if((activeTime&index)!=0){
                  r.push(i);
              }
              index=index<<1;
          }
          return r;
      },
      time2Str(timeArr){
          let start = null;
          let timeStrArray = [];
          for(let i = 1;i<=24;i++){
              if(timeArr.indexOf(i)!=-1){
                    if(!start){
                        start = i;
                    }
              }else{
                  if(start){
                      //输出start到i的时间字符串
                      let left = start-1<10? "0"+(start-1):(start-1);
                      left += ":00";
                      let right = i-1<10? "0"+(i-1):(i-1);
                      right += ":00";
                      timeStrArray.push(left+"~"+right);
                      start = null;
                  }
              }
          }
          if(start){
              //输出start到i的时间字符串
              let left = start-1<10? "0"+(start-1):(start-1);
              left += ":00";
              timeStrArray.push(left+"~24:00");
              start = null;
          }
          return timeStrArray;
      },
      getTimeStrByBits(bitInt){
          let timeArr = this.getTime(bitInt);
          return  this.time2Str(timeArr)
      }
  },
    mounted: function () {

    }
})