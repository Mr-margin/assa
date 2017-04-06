$.validator.setDefaults({
    highlight: function(e) {
        $(e).closest(".form-group").removeClass("has-success").addClass("has-error")
    },
    success: function(e) {
        e.closest(".form-group").removeClass("has-error").addClass("has-success")
    },
    errorElement: "span",
    errorPlacement: function(e, r) {
        e.appendTo(r.is(":radio") || r.is(":checkbox") ? r.parent().parent().parent() : r.parent())
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"
});
$.validator.addMethod("isZipCode", function(value, element) {   
    var tel = /^\w+$/;
    return this.optional(element) || (tel.test(value));
}, "只能填写英文字母、数字和下划线");
toastr.options = {
	"closeButton": true,
	"debug": false,
	"progressBar": false,
	"positionClass": "toast-top-right",
	"onclick": null,
	"showDuration": "400",
	"hideDuration": "1000",
	"timeOut": "6000",
	"extendedTimeOut": "1000",
	"showEasing": "swing",
	"hideEasing": "linear",
	"showMethod": "fadeIn",
	"hideMethod": "fadeOut"
}

//刷新
function shuaxin(form){
	$(form).find("input").each(function(){
		  var id = $(this).attr("id");
		  $("#"+id).val("");
	});
	$(form).find("textarea").each(function(){
		var id = $(this).attr("id");
		$("#"+id).val("")
	});
}

//获取选中行数据
function getSelectedRow(form) {
    var index = form.find('tr.success').data('index');
    return form.bootstrapTable('getData')[index];
}

//标准参数配置
function queryParams(params) {
	var temp = {};
    temp.pageSize = params.limit;
    temp.pageNumber = params.offset;
    temp.search = params.search;
    return temp;
}