angular.module('gmaDiaries', [])
	.controller('home', function($scope, $http) {
		$http.get('/resource/').success(function(data) {
			$scope.gmaArtifact = data;
		})
	});