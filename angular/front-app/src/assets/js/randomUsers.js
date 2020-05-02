var randomUser = randomUser || (function () {
    var show = true;

	function slideAll(){
		$('.user-profile').each(function () {			
			$(this).fadeToggle('slow');
		})
	}

	return {
        showDetailUser: function (elment) {
            
			var parentElement = $(elment).closest('.user-profile');
			var anotherFields = $(parentElement).find('.another-fields').clone();
			var button = $(parentElement).find('.btn-profile');
			
			var functionTextButton = function(){
				button.text(
					($.trim(button.text()) == "View profile") ? "Close" : "View profile"
				);
			}
			
			if(show){
				$(parentElement).fadeToggle('slow', 'swing', function(){
					functionTextButton();
				});
				slideAll();
			} else{
				$(parentElement).fadeToggle('slow', 'swing', function(){
					functionTextButton();
					slideAll();
				});
			}
			show = !show;
			
			$('#detail').toggleClass('d-none').html('').append(anotherFields);
        }
    };
})();