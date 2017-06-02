function getSelected(type){
  //find the selected folder
    var selected_folder;
    var folders = document.getElementsByName("folder");
    for(var i =0; i < folders.length; i++){
      if(folders[i].checked){
        selected_folder = folders[i];
      }
    }
    return selected_folder.nextElementSibling;
}


function decorateSelected(element,type){
  //to add styles either to the selected folder or the project

  if(type == "folder"){
    var folders = document.getElementsByName("folder");
    for(var i =0; i < folders.length; i++){

        folders[i].setAttribute("class","folder");

    }
    element.setAttribute("class","folder active");
  }
  else if(type == "project"){
    var projects = document.getElementsByClassName(".project");
    for(var i =0; i < projects.length; i++){
      projects[i].setAttribute("class","project");
    }
    document.getElementById(element).setAttribute("class","project active")
  }

}
