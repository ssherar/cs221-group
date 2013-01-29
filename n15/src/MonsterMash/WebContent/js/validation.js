/**
 * A generic javascript validator, which uses html elements such as
 * a data-set or classes.
 * 
 * Author: Sam Sherar <sbs1@aber.ac.uk>
 * 
 * Class selectors:
 *   - required
 *   		Make sure that the field not empty
 *   - numeric
 *   		Tests if it's numeric
 * Dataset selectors
 * 	 - minlength
 * 			Checks if the length is not less than the value given
 * 
 * Example usage:
 * <input type="text" name="foo" data-minlength="3" class="required" />
 */
function validate(formVar) {
	var mesg = ""
	for(i = 0; i < formVar.elements.length; i++) {
		ele = formVar.elements[i];
		if(ele.className != "") {
			if(ele.className == "required") {
				if(ele.value == null || ele.value == "") {
					mesg += ele.name + " is empty! \n";
				}
			} 
			if(ele.className == "numeric") {
				if(isNaN(ele.value)) {
					mesg += ele.name + " is not numeric \n";
				}
			}	
		}
		if(ele.dataset.minlength != undefined) {
			if(ele.value.length < parseInt(ele.dataset.minlength)) {
				mesg += ele.name + " is too short! Minimum: " + ele.dataset.minlength + " \n";
			}
		}
	}
	if(mesg != "") {
		alert(mesg);
		return false;
	} else {
		return true;
	}
}