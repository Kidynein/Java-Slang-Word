# Sơ Đồ Thiết Kế (UML Class Diagram)

Đây là sơ đồ lớp mô tả kiến trúc của ứng dụng, bao gồm mô hình MVC và pattern Singleton.

```mermaid
classDiagram
    direction BT

    class Launcher {
         +main(String[] args)
    }

    class MainApplication {
        <<Application>>
        +start(Stage primaryStage)
    }
    
    class SlangDictionary {
        <<Singleton>>
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
    
    class MainController {
        -mainPane: BorderPane
        +initialize()
        +handleShowSearch()
        +handleShowQuiz()
        +handleShowHistory()
        +handleReset()
    }
    
    class SearchController {
        -dictionary: SlangDictionary
        -searchField: TextField
        -resultView: ListView
        +initialize()
        +handleSearchButton()
    }

    class QuizController {
        -dictionary: SlangDictionary
        -questionLabel: Label
        -answerButton1: Button
        +initialize()
        +loadNewQuiz()
        +handleAnswerClick()
    }

    Launcher ..> MainApplication : calls main()
    MainApplication ..> MainController : (1) loads main-view.fxml
    
    MainController "1" o-- "1" SlangDictionary : (2) uses (gets instance)
    SearchController "1" o-- "1" SlangDictionary : (3) uses (gets instance)
    QuizController "1" o-- "1" SlangDictionary : (3) uses (gets instance)
    
    MainController ..> SearchController : (4) loads search-view.fxml
    MainController ..> QuizController : (4) loads quiz-view.fxml