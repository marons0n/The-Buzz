import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:my_tutorial_app/net/webRequests.dart';
import 'package:my_tutorial_app/models/IdeaItem.dart'; 
import 'package:my_tutorial_app/main.dart';

// Mocking the submitNewIdea function
class MockSubmitNewIdea extends Mock {
  Future<void> call(String idea) async {
    // This is just a placeholder for what the mock should do.
    // You can leave it empty or add specific behavior for testing.
    return Future.value(); // Return a completed Future<void>
  }
}


void main() {
  testWidgets('Test new idea posting in HttpReqWords in main', (WidgetTester tester) async {
    // Arrange
    final mockSubmitNewIdea = MockSubmitNewIdea();
    
    // Replace the function submitnewideafunction with the mock
    submitNewIdeaFunction = mockSubmitNewIdea;

    // Build our app and trigger a frame.
    await tester.pumpWidget(const MyApp());

    // Verify the initial state
    // expect(find.text(''), findsOneWidget, reason: 'Expected to find a placeholder when no ideas are present.');

    // Input some text into the TextField
    final textFieldFinder = find.byType(TextField);
    final submitButtonFinder = find.byType(IconButton).first;

    // Act
    await tester.enterText(textFieldFinder, 'New Idea');
    await tester.tap(submitButtonFinder);
    await tester.pumpAndSettle(); // Wait for any animations to complete

    // Assert that the submit function was called with the correct argument
    verify(mockSubmitNewIdea.call('New Idea')).called(1);
    
    // expect(find.text('New Idea'), findsOneWidget);
  });
}