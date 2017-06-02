
// catch the modes
var fileMode = document.getElementsByName("modes")[2];

// for charts
var packages = [];
var packageValues = [];
var datas = [];
var colors = ["ae3800", "f8a657", "4b10ac", "e93862", "005eff", "620f6d", "9b8c2b", "056493", "668ed0", "0d6856", "646464", "3dc34b", "a47d7c", "9258f1", "c52119", "d91997", "7b9191", "d87d34", "547724", "1dadba", "ce990a", "3fd3b6", "e43e29", "1692cf", "347c85", "866817", "d27960", "5a64cc", "504367", "852630", "93d21c", "9f5e4d", "730451", "dcb907", "0dcb7d", "f76605", "a05ea8", "166dbf", "576d77", "3c0479", "479b8c", "9d6d88", "65765a", "f61111", "417663", "c0504d", "f5cf0e", "9bbb59", "4a452a", "ff8500"];


//listening to events
fileMode.addEventListener("click",function(event){
  var request = new XMLHttpRequest;
  request.open("get","js/packageClass.json");
  // request.open("get","");
  request.send(null);
  request.onreadystatechange = function(){
    if(request.readyState == XMLHttpRequest.DONE){
      console.log(request.responseText);
      var responses = JSON.parse(request.responseText);
      for(var i in responses){
        var response = responses[i];
        var attrValues = response.attributeValues;
        var packageValues = [];
        packages = [];
        for( var key in attrValues){
          packages.push(key);
          packageValues.push(attrValues[key]);
        }
        var entry ={};
        entry.label = response.attributeName;
        entry.data = packageValues;
        entry.backgroundColor =
            'rgba(255, 99, 132, 0.12)';
        entry.borderColor =
            '#'+colors[i];
        entry.borderWidth= 1;
        datas.push(entry);
      }
      drawChart();
    }
  }
})

function drawChart(){


var ctx = document.getElementById("myChart");
var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: packages,
        // datasets: [{
        //   // label: (function(){return attrLabel.label})(),
        //     label: attrLabel.test,
        //     data: packageValues,
        //     // backgroundColor: [
        //     //     'rgba(255, 99, 132, 0.2)',
        //     //     'rgba(54, 162, 235, 0.2)',
        //     //     'rgba(255, 206, 86, 0.2)',
        //     //     'rgba(75, 192, 192, 0.2)',
        //     //     'rgba(153, 102, 255, 0.2)',
        //     //     'rgba(255, 159, 64, 0.2)'
        //     // ],
        //     // borderColor: [
        //     //     'rgba(255,99,132,1)',
        //     //     'rgba(54, 162, 235, 1)',
        //     //     'rgba(255, 206, 86, 1)',
        //     //     'rgba(75, 192, 192, 1)',
        //     //     'rgba(153, 102, 255, 1)',
        //     //     'rgba(255, 159, 64, 1)'
        //     // ],
        //     backgroundColor:
        //         'rgba(255, 99, 132, 0.2)',
        //     borderColor:
        //         'rgba(255,99,132,1)',
        //     borderWidth: 1
        // }],
        datasets:datas,
    },

    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});
}
