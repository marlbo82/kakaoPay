var app = angular.module('app', []);
app.controller('controller', function($scope, $http) {

	$scope.createTodoWork = function(){
		$http({
			method:'POST',
			url:'/test/works',
			data : {
		        'workTitle' : workTitle.value, 
		        'uprWorkId' : uprWorkId.value
			}
		}).then(function(response, data){
			if (response.data.resultCode != 200) { 
				console.log('등록 실패');
				alert('등록 실패 (' + response.data.resultMsg + ")");
			} else {
				console.log('등록 성공');
				alert('등록 성공 (' + response.data.resultMsg + ")");
			}
			workTitle.value = '';
			uprWorkId.value = '';
			$scope.getTodoList($scope.presentPage);
		});
	}
	
	$scope.getTodoList = function(page){
		$scope.presentPage = page;
		
		$http({
			method:'GET',
			url:'/test/works?page=' + $scope.presentPage
		}).then(function(response, data){
			console.log('조회 성공');
			$scope.records = [];
			$scope.totalPages = [];
			for (var i = 0; i < response.data.data.length; i++) {
				$scope.records.push(response.data.data[i]);
			}
			
			for (var i = 0; i < response.data.data[0].totalPage; i++) {
				$scope.totalPages.push(i+1);
			}
			
			document.getElementById("pagebtn_"+($scope.presentPage-1)).style.fontWeight = 'bold';
			if ($scope.prePage != undefined) {
				document.getElementById("pagebtn_"+($scope.prePage-1)).style.fontWeight = 'normal';
			}
			$scope.prePage = page;
		});
	}
	
	$scope.updateTodoWork = function(index){
		var workId = (($scope.presentPage-1) * 4) + index + 1; 
		$http({
			method:'PUT',
			url:'/test/works',
			data : {
				'workId' : workId,
				'workTitle' : document.getElementById("workTitle_"+index).value, 
				'uprWorkId' : document.getElementById("uprWorkId_"+index).value,
				'firstRegDtm' : $scope.records[index].firstRegDtm
			}
		}).then(function(response, data){
			if (response.data.resultCode != 200) { 
				console.log('수정 실패');
				alert('수정 실패 (' + response.data.resultMsg + ")");
			} else {
				console.log('수정 성공');
			}

			$scope.getTodoList($scope.presentPage);
		});
	}
	
	$scope.compelteTodoWork = function(index){
		var workId = (($scope.presentPage-1) * 4) + index + 1; 
		$http({
			method:'PUT',
			url:'/test/works?isCompletion=true',
			data : {
				'workId' : workId,
		        'workTitle' : $scope.records[index].workTitle, 
		        'uprWorkId' : $scope.records[index].uprWorkId,
		        'firstRegDtm' : $scope.records[index].firstRegDtm
			}
		}).then(function(response, data){
			if (response.data.resultCode != 200) { 
				console.log('완료 처리 실패');
				alert('완료 처리 실패 (' + response.data.resultMsg + ")");
			} else {
				console.log('완료 처리 성공');
			}
			
			$scope.getTodoList($scope.presentPage);
		});
	}
	
	$scope.isNull = function(index){
		if($scope.records[index].completeDtm == '' || $scope.records[index].completeDtm == undefined || $scope.records[index].completeDtm == null) {
			return false;
		} else {
			return true;
		}
	}
	
	$scope.getTodoList(1);
});