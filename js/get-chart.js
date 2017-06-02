var colors = ["ae3800", "f8a657", "4b10ac", "e93862", "005eff", "620f6d", "9b8c2b", "056493", "668ed0", "0d6856", "646464", "3dc34b", "a47d7c", "9258f1", "c52119", "d91997", "7b9191", "d87d34", "547724", "1dadba", "ce990a", "3fd3b6", "e43e29", "1692cf", "347c85", "866817", "d27960", "5a64cc", "504367", "852630", "93d21c", "9f5e4d", "730451", "dcb907", "0dcb7d", "f76605", "a05ea8", "166dbf", "576d77", "3c0479", "479b8c", "9d6d88", "65765a", "f61111", "417663", "c0504d", "f5cf0e", "9bbb59", "4a452a", "ff8500"];

function draw_chart(labels,data){
  var ctx = document.getElementById("myChart");
  var myChart = new Chart(ctx, {
      type: 'bar',
      data: {
          labels: labels,
          datasets: [{
              // label: 'package',
              data: data,
              backgroundColor: [
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 206, 86, 0.2)',
                  'rgba(75, 192, 192, 0.2)',
                  'rgba(153, 102, 255, 0.2)',
                  'rgba(255, 159, 64, 0.2)'
              ],
              borderColor: [
                  'rgba(255,99,132,1)',
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 206, 86, 1)',
                  'rgba(75, 192, 192, 1)',
                  'rgba(153, 102, 255, 1)',
                  'rgba(255, 159, 64, 1)'
              ],
              borderWidth: 1
          }]
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

//to parse and draw charts for package data
function package_chart(chart_data){
  chart_data = JSON.parse(chart_data);
  var labels = [];
  var data = [];
  for(var i =0; i<chart_data.length-1; i+=2){
    labels.push(chart_data[i].attributeName);
    data.push(chart_data[i+1].attributeValues.console);
    console.log(chart_data[i].attributeName,chart_data[i+1].attributeValues.console);
  }
  draw_chart(labels,data);
}



//to parse and draw charts for class data
function chart(chart_data){
  chart_data = JSON.parse(chart_data);
  //get the files for which the parsing has been done
  var values = chart_data[1].attributeValues;
  var files = [];
  for(var key in values){
    key = key.split("/").pop();
    files.push(key);
  }

  //store the key value pairs for all files for all parameters
  var datasets = []; //holds all entries
  for(var i = 0; i<chart_data.length; i=i+2){
    var data = [];
    for(var key in chart_data[i+1].attributeValues){
      data.push(chart_data[i+1].attributeValues[key]);
    }
    var entry = []; //every entry will have the attributeName as the label and the attributeValues as the data
    entry.label = chart_data[i].attributeName;
    entry.data = data;
    entry.backgroundColor = 'rgba(255, 99, 132, 0.12)';
    entry.borderColor =
        '#'+colors[i];
    entry.borderWidth= 1;
    datasets.push(entry);
  }
  // console.log(datasets);
  Test.data = datasets;
  Test.labels = files;
  process();
  var ctx = document.getElementById("myChart");
  var myChart = new Chart(ctx, {
      type: 'bar',
      data: {
          labels: files,
          datasets: datasets,
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

var Test = {}
function process() {
  var collection = {};
  for(var i in Test.data){
    var data = Test.data[i];
    collection[data.label]=data.data;
  }

  for(var i = 0 ; i < collection.length; i++){

  }

}


function drawTable(responseArray) {
  responseArray = JSON.parse(responseArray);
  var values = responseArray[1].attributeValues;

  var table = document.createElement('table');
  var th = document.createElement('tr');
  table.appendChild(th);
  var td = createTd(th);
  td.innerText = "Files";

  var trs = {};
  for(var key in values){
    var tr = document.createElement('tr');
    table.appendChild(tr);
    var td = createTd(tr);
    trs[key] = tr;

    key = key.split("/").pop();
    td.innerText = key;


  };

  for (var i = 0; i < responseArray.length; i+=2) {
    var head = responseArray[i].attributeName;
    var td = createTd(th);
    td.innerText = head;

    var records = responseArray[i+1].attributeValues;
    for(var key in records){
        var td = createTd(trs[key]);
        td.innerText = records[key];
    }
  }

  Test = table;
  var container = document.querySelector(".table-content");
  var table_exists = document.querySelector(".table-content table");
  if(table_exists){
      container.replaceChild(table,table_exists);
  }
  else{
    container.appendChild(table);
  }


}

function createTd(tr) {
  var td = document.createElement('td');
  tr.appendChild(td);
  return td;
}

function get_chart(data, type = "class"){
  if(type == "package"){
    package_chart(data);
  }
  else{
    chart(data);
  }
}

// function get_table(data, type = "class"){
//   data = JSON.parse(data);
//   // console.table(data);
//   drawTable(data);
//   var headers = [];
//
//   //get the files for which the parsing has been done
//   var values = data[1].attributeValues;
//   var files = [];
//   for(var key in values){
//     // key = key.split("/").pop();
//     files.push(key);
//   }
//   console.log(files.length);
//   //create a table
//   var table = document.createElement("table");
//
//   //get headers for the table
//   var th = document.createElement("th");
//
//   //let the first column be file which will have the list of files
//   var td = document.createElement("td");
//   var text = document.createTextNode("File");
//   td.appendChild(text);
//   th.appendChild(td);
//   for(var i = 0; i< data.length; i=i+2){
//     var td = document.createElement("td");
//     var text = document.createTextNode(data[i].attributeName);
//     td.appendChild(text);
//     th.appendChild(td);
//   }
//   table.appendChild(th);
//
//   // create rows for every file
//   for(var i = 0; i< files.length; i++){
//       var tr = document.createElement("tr");
//       // the first column being the file
//       var td = document.createElement("td");
//       var text = document.createTextNode(files[i]);
//       td.appendChild(text);
//       tr.appendChild(td);
//       var attr_value = data[i].attributeValues;
//       for(var key in attr_value){
//         console.log();
//       }
//
//   }
//
//   for(var i  = 1 ; i< data.length-2; i=i+2){
//     var td = document.createElement("td");
//   }
//
// console.log(table);
//
// }


function updateview(mode){
  localStorage.setItem('view_mode',mode);
}

function get_project(project_id,get_data = 'class'){

    //if there is a value project id value in the param, it means am requesting
    //for a new project

    //if there is no value for project id in the params, it means am requesting
    //data for the existing project

  if(project_id != ''){ //there exists a value for project id
    localStorage.setItem('active_project_id',project_id);
    document.querySelector("input[name='modes']#class").checked = true;
  }
  else{ //the project value is empty
    project_id = localStorage.getItem('active_project_id');
  }
  var data = new FormData();
  data.set("project_id",project_id);
  data.set("get_data",get_data);
  var request = new XMLHttpRequest;
  request.open("post","apis/get-project-data.php");
  request.send(data);
  request.onreadystatechange = function(){
    if(request.readyState == XMLHttpRequest.DONE){
      var response = JSON.parse(request.responseText);
        get_chart(response.data,get_data);
        drawTable(response.data);
        var selected_project = document.querySelector("#"+project_id);
        decorateSelected(project_id,"project");
    }
  }
}
