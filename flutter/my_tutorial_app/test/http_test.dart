// import 'package:flutter/material.dart';
// import 'package:flutter_test/flutter_test.dart';
// import 'package:mockito/mockito.dart';
// import 'package:my_tutorial_app/net/webRequests.dart';
// import 'package:my_tutorial_app/models/IdeaItem.dart'; 
// import 'package:my_tutorial_app/main.dart';


// class MockWebRequests extends Mock implements webRequests {}

// void main() {
//   testWidgets('Test POST functionality in HttpReqWords', (WidgetTester tester) async {
//     // Arrange
//     final mockWebRequests = MockWebRequests();

//     // Override the actual submitNewIdea with the mock
//     when(mockWebRequests.submitNewIdea(any)).thenAnswer((_) async => Future.value());

//     // Build the widget tree
//     await tester.pumpWidget(
//       MaterialApp(
//         home: Scaffold(body: HttpReqWords()), // Use your actual widget
//       ),
//     );

//     // Act
//     final textFieldFinder = find.byType(TextField);
//     final submitButtonFinder = find.byType(IconButton).first; // Assuming the first IconButton is the submit button

//     // Input some text
//     await tester.enterText(textFieldFinder, 'New Idea');
//     await tester.tap(submitButtonFinder);

//     // Wait for the submission to complete
//     await tester.pumpAndSettle();

//     // Assert
//     verify(mockWebRequests.submitNewIdea('New Idea')).called(1);
//     expect(find.text('New Idea'), findsOneWidget); // Assuming it will display somewhere
//   });
// }