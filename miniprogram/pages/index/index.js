//index.js
const app = getApp()

Page({

  data:{
    step :0,
    bus:{},
    walk:{},
    pass:'',
    depart:'',
    arrive:'',
    index:['walk','bus'],
    value : new Array(),
    method:["步行","校园巴士"],
    preference:["步行","校园巴士"],
    preferencelist: new Array(),
    searchtxt:'',
    datares: new Array(),
    markers: [
    {
      iconPath: "/mark/7.PNG",
      id: 0,
      latitude: 31.020502,//31.029236,
      longitude: 121.434009,//121.452591,
      width: 50,
      height: 50,
    }
    ],
    currentdata:new Array(),



  }  ,
    onLoad:function(){
    var that = this

    that.setData({ 
         pass:'',
    depart:'',
    arrive:'',})

    wx.request({
      url: 'https://api.ltzhou.com/map/nearby/parking',
      data:{"lat":"31.021807" ,"lng":"121.429846"},
      success(res){
        var x

        var markers=new Array();
        var q = 0
        for (x in res.data)
        {
          if (res.data[x].bikeCount)
          {         var marker ={iconPath: "/mark/19.PNG",
          id: q,
          latitude: 31.021807,//31.029236,
          longitude: 121.429846,//121.452591,
          width: 50,
          height: 50,
          name:'',
          bikeCount:''}
            marker.latitude=res.data[x].vertexInfo.location.coordinates[1]
            marker.longitude=res.data[x].vertexInfo.location.coordinates[0] 
            marker.name=res.data[x].vertexInfo.vertexName 
            marker.bikeCount=res.data[x].bikeCount
            marker.iconPath = "/mark/"+res.data[x].bikeCount+".PNG"
            console.log(marker)
            markers.push(marker) 
            console.log("adding")
            console.log(markers)
           q =q +1}}      
         console.log(markers)
        that.setData({markers:markers})}
    })
  
  
  
  
  
  
  
  },
    pass:function(e){
      this.setData({
        pass: e.detail.value,
      })
    },
    depart:function(e){
      this.setData({
        depart: e.detail.value,
      })
    },
    arrive:function(e){
      this.setData({
        arrive: e.detail.value,
      })
    },
    indexback: function(e)
    {this.setData({step:0})
    },
    formSubmit: function (e) {
      var depart=String( this.data.depart)
      var arrive=String( this.data.arrive)
      var pass=this.data.pass
      var passlist=[];
      if(pass){
      passlist.push(pass)};
      var that =this;
      var tem;
      var valuetem=new Array();
      var pre = new Array();
      var i;
      var j = 0;
      var preres = new Array();
      console.log({
        "arrivePlace": arrive,
        "beginPlace": depart,
        "departTime": "2020/05/11 12:05:12",
        "passPlaces": passlist,})
      wx.request({
        url: 'https://api.ltzhou.com/navigate/bus',
        method:'POST',
        header: {
        'content-type': 'application/json'
        },
        data:{
        "arrivePlace": arrive,
        "beginPlace": depart,
        "departTime": "2020/05/11 12:05:12",
        "passPlaces": passlist,},

        success (res) {
          tem = res.data
          console.log(tem)
          that.setData({bus:tem})
          valuetem.push(tem)
          wx.request({
            url: 'https://api.ltzhou.com/navigate/walk',
            method:'POST',
            header: {
            'content-type': 'application/json'},
          data:{
            "arrivePlace": arrive,
            "beginPlace": depart,
            "passPlaces": passlist,
            },
            success (res) {
              tem = res.data
              console.log(tem)
              valuetem.push(tem)
              that.setData({value:valuetem})
              console.log(that.data.value)
              console.log("1")
              for(j=0;j<that.data.preference.length;j++){
                for (i=0;i<that.data.value.length;i++){
                  if(that.data.value[i].type==that.data.preference[j]){pre.push(i)}}}
              for (i=0;i<pre.length;i++){preres.push(that.data.value[pre[i]])}
              console.log(preres)
              var ressss = new Array()
              ressss.push(preres)
              ressss.push(valuetem)
              ressss.push(that.data.bus.routeplan)
              ressss.push(depart)
              ressss.push(pass)
              ressss.push(arrive) 
              that.setData({datares:ressss})
              console.log(that.data.datares)

              wx.navigateTo({
                url: '../searcha/searcha?RT='+JSON.stringify(that.data.datares),
                //success:function(res){that.setData({step:0})}
              
              },

                )

            }}
              )             
    }})},
  navigatePage:function()
  { wx.setStorage({
    data:'',
    key: 'arrive',
  })  
  wx.setStorage({
    data:'',
    key: 'pass',
  })  
  wx.setStorage({
    data:'',
    key: 'depart',
  })  
    wx.navigateTo({
    url: '../searchindex/searchindex',
  })
  },

  setnavigatePage:function()
  { wx.setStorage({
    data:{name:this.data.markers[this.data.currentdata].name,
        
    },
    key: 'arrive',
  })  
  wx.setStorage({
    data:'',
    key: 'pass',
  })  
  wx.setStorage({
    data:'',
    key: 'depart',
  })  
    wx.navigateTo({
    url: '../searchindex/searchindex',
  })
  },

  searchInput:function(e)
  {    app.globalData.search =e.detail.value
    
  },
  search:function()
  {    
  },


  showModal: function (e) {
    console.log(e.markerId)
    this.setData({currentdata:e.markerId})
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },

  //隐藏弹框
  hideModal: function () {
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false
      })
    }.bind(this), 200)
  },


})
