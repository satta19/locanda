$(function () {
    //---  ACCOMODATION SECTION CODE    
    $.Class.extend('Accomodation', /* @prototype */ {
        init: function () { 
        	var self = this;
        	/* Buttons rendering and event handler attachments */
            $(".btn_add_new").button({
                icons: {
                    primary: "ui-icon-circle-plus"
                }
            }).click(function () {
                window.location.href = "goAddNewRoom.action?sect=accomodation";
                return false;
            });
            $(".btn_delete_room").click(function (event) {
                $(this).parents(".yform").attr("action", "deleteRoom.action");
            });
            $(".btn_add_facility_room").button({
                icons: {
                    primary: "ui-icon-circle-plus"
                }
            }).click(function (event) {
                //-- event.preventDefault();
                var url_action_facility = "goRoomFacilities_edit";
                var id_room = $(this).parents(".yform").find('input:hidden[name="room.id"]').val();
                var name_room = $(this).parents(".yform").find('input:text[name="room.name"]').val();
                $.ajax({
                    type: 'POST',
                    url: url_action_facility,
                    data: {
                        idRoom: id_room
                    },
                    success: function (data) {
                        $("#facility_edit_dialog").html(data);
                        $("#facility_edit_dialog").dialog({
                            title: "Add Facility for room: " + name_room,
                            modal: true,
                            buttons: {
                                "Save": function () {
                                    $(this).find(".yform.json").submitForm();
                                    $(this).dialog("close");
                                },
                                cancel: function () {
                                    $(this).dialog("close");
                                }
                            }
                        });
                    },
                    error: function () {
                        $().notify($.i18n("seriousError"), $.i18n("seriousErrorDescription"));
                    }
                });
                return false;
            });
            //button click handler for delete all rooms
            $(".btn_save_all_rooms").click(function (event) {
                var allRedirectInputs = $(".yform.json").find('input:hidden[name="redirect_form"]');
                var redirectOld = allRedirectInputs.last().val();
                allRedirectInputs.val("");
                $(".yform.json").submit();
                allRedirectInputs.val(redirectOld);
            });
            $(".min_stay").click(function () {
                if ($(this).hasClass("clickable")) {
                    $(".price_show").addClass("clickable");
                    $(this).removeClass("clickable");
                    $(".price_type").hide();
                }
            });
            $(".price_show").click(function () {
                if ($(this).hasClass("clickable")) {
                    $(".min_stay").addClass("clickable");
                    $(this).removeClass("clickable");
                    $(".price_type").show();
                }
            });
            $("#roomType").change(function () {
                var selectedId = $(this).find(":selected").val();
                if (typeof parseInt(selectedId) == "number" && parseInt(selectedId) > 0) {
                	
                	 Models.RoomFacility.findAll({'room.roomType.id': selectedId}, self.callback('listRoomFacilities'),self.callback('listRoomFacilitiesError'));
                }
            });
            //add notify functionality as tooltip for input text
            $("#room_name_id, #max_guests_id, #price_room_id").mousedown(function () {
                $.jGrowl("close");
                $.jGrowl($(this).nextAll("span:hidden").text(), {
                    position: "top-left"
                });
            }).mouseout(function () {
                $.jGrowl("close");
            });
            $(".accomodation_tree").jstree({
                "core": {
                    "initially_open": ["root"]
                },
                "json_data": {
                    "ajax": {
                        "url": "findAllTreeRoomsJson.action"
                    }
                },
                "themes": {
                    "theme": "default",
                    "dots": true,
                    "icons": true
                },
                "plugins": ["themes", "json_data"]
            });
        },
        
        /**
         * Displays a list of RoomFacilities.
         * @param String.
         */
         listRoomFacilities: function( roomFacilitiesHtml ){
             $(".wrapper-facility").empty();
             $(".wrapper-facility").append(roomFacilitiesHtml);
         },
         
         /**
          * Display error advice for RoomFacilities.
          * @param {null}.
          */
          listRoomFacilitiesError: function(  ){
        	  $().notify($.i18n("seriousError"), $.i18n("seriousErrorDescription"));
          }
    
    
    
    
    
    });
    //---  END ACCOMODATION SECTION CODE 
    
    
    new Accomodation();
});