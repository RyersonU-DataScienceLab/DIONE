function get_table(project_id){
  var data = new FormData();
  data.set("project_id",project_id);
  var request = new XMLHttpRequest;
  request.open("post","get-table.php");
  request.send(data);
  request.onreadystatechange = function(){
    if(request.readyState == XMLHttpRequest.DONE){
      console.log(data);
    }
  }
}
