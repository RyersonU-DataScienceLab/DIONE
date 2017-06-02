var create_folder_form = document.getElementById("new-folder");
var root_folder = "";
create_folder_form.addEventListener("submit",function(event){
  event.preventDefault();

  //identify the selected folder
  var folders = document.getElementsByName("folder");
  for (var i = 0; i < folders.length; i++) {
    if(folders[i].checked){
      root_folder = folders[i].id;
    }
  }
  console.log(root_folder);

  // var folders = document.getElementsByClassName("folder-arch")[0].getElementsByTagName("label");
  // for(var folder in folders){
  //   if(folder.htmlFor == root_folder){
  //     root_folder = folder;
  //   }
  // }
  var data = new FormData(create_folder_form);
  data.set("parent-folder",root_folder);
  var folder_text = data.get("folder_name");
  var request = new XMLHttpRequest();
  request.open("post","apis/create-folder.php");
  request.send(data);
  request.onreadystatechange = function(){
    if(request.readyState == XMLHttpRequest.DONE){


            if(request.responseText != "error"){
              //creating a checkbox for action
              var folder_id = request.responseText;
              var folder_action  = document.createElement("input");
              folder_action.setAttribute("id",folder_id);
              folder_action.setAttribute("type","checkbox");
              folder_action.setAttribute("name","folder");
              folder_action.setAttribute("onclick","show_folder_contents('this')");

              //creating a folder
              var folder = document.createElement("label");
              folder.setAttribute("for",folder_id);
              folder.setAttribute("class","folder");
              var folder_name = document.createTextNode(folder_text);
              folder.appendChild(folder_name);

              //creating folder container - for children
              var folder_container = document.createElement("div");
              folder_container.setAttribute("class","folder-container");

              //creating container for above elements
              var f_container = document.createElement("div");
              f_container.setAttribute("id","f_"+folder_id);
              f_container.setAttribute("class","f_container");
              f_container.appendChild(folder_action);
              f_container.appendChild(folder);
              f_container.appendChild(folder_container);


              //appending to the parent container
              //var container = document.getElementById("f_"+parent_folder_id);
              var container = document.getElementById("f_"+root_folder);
              console.log(container);
              console.log(root_folder);
              container = container.getElementsByClassName("folder-container")[0];
              container.appendChild(f_container);
              document.getElementsByName("new_folder")[0].checked = false;
            }
            else{

            }

    }
  }
});
