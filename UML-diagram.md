# Sơ Đồ Thiết Kế (UML Class Diagram)

Đây là sơ đồ lớp mô tả kiến trúc của ứng dụng, bao gồm mô hình MVC và pattern Singleton.

```mermaid
classDiagram
direction BT

%% 1. Launchers
class Launcher {
    +main(String[] args)
}

class MainApplication {
    +start(Stage primaryStage)
}

%% 2. Model
class SlangDictionary {
    -instance: SlangDictionary (static)
    -slangMap: HashMap~String, List~String~~
    -searchHistory: List~String~
    -SlangDictionary() (private)
    +getInstance(): SlangDictionary (static)
    +findBySlang(String slang): List~String~
    +findByDefinition(String keyword): Map~String, List~String~~
    +addSlang(String slang, String def, boolean overwrite)
    +editSlang(String oldSlang, String newDef)
    +deleteSlang(String slang)
    +resetDictionary()
    +getRandomSlang(): String
    +getHistory(): List~String~
    +createQuiz(boolean slangAsQuestion): Object
    +saveData()
    -loadData()
    -loadFromTextFile()
}

%% 3. Main Controller
class MainController {
    -mainPane: BorderPane
    +initialize()
    +handleShowSearch()
    +handleShowQuiz()
    +handleShowHistory()
    +handleShowAdd()
    +handleShowEdit()
    +handleShowDelete()
    +handleShowRandom()
    +handleReset()
}

%% 4. Sub-Controllers
class SearchController {
    -dictionary: SlangDictionary
    -searchField: TextField
    -resultView: ListView
    +initialize()
    +handleSearchButton()
}

class HistoryController {
    -dictionary: SlangDictionary
    +initialize()
}

class AddController {
    -dictionary: SlangDictionary
    +initialize()
    +handleAddButton()
}

class EditController {
    -dictionary: SlangDictionary
    -originalSlang: String
    +initialize()
    +handleFindButton()
    +handleSaveButton()
}

class DeleteController {
    -dictionary: SlangDictionary
    +initialize()
    +handleDeleteButton()
}

class RandomController {
    -dictionary: SlangDictionary
    +initialize()
    +handleRandomButton()
}

class QuizController {
    -dictionary: SlangDictionary
    -questionLabel: Label
    -answerButton1: Button
    +initialize()
    +loadNewQuiz()
    +handleAnswerClick()
}

class DefQuizController {
    -dictionary: SlangDictionary
    -questionLabel: Label
    -answerButton1: Button
    +initialize()
    +loadNewQuiz()
    +handleAnswerClick()
}

class WelcomeController {
    +initialize()
}

%% 5. Relationships
Launcher ..> MainApplication : calls main()
MainApplication ..> MainController : loads main-view.fxml

MainController "1" o-- "1" SlangDictionary : gets instance
SearchController "1" o-- "1" SlangDictionary : gets instance
HistoryController "1" o-- "1" SlangDictionary : gets instance
AddController "1" o-- "1" SlangDictionary : gets instance
EditController "1" o-- "1" SlangDictionary : gets instance
DeleteController "1" o-- "1" SlangDictionary : gets instance
RandomController "1" o-- "1" SlangDictionary : gets instance
QuizController "1" o-- "1" SlangDictionary : gets instance
DefQuizController "1" o-- "1" SlangDictionary : gets instance
WelcomeController "1" o-- "1" SlangDictionary : gets instance

MainController ..> SearchController : loads search-view.fxml
MainController ..> QuizController : loads quiz-view.fxml
MainController ..> HistoryController : loads history-view.fxml
MainController ..> AddController : loads add-view.fxml
MainController ..> EditController : loads edit-view.fxml
MainController ..> DeleteController : loads delete-view.fxml
MainController ..> RandomController : loads random-view.fxml
MainController ..> DefQuizController : loads defquiz-view.fxml
MainController ..> WelcomeController : loads welcome-view.fxml
