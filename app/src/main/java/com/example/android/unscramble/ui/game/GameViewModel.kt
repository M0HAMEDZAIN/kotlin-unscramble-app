import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.ui.game.MAX_NO_OF_WORDS
import com.example.android.unscramble.ui.game.SCORE_INCREASE
import com.example.android.unscramble.ui.game.allWordsList

/**
 * ViewModel containing the app data and methods to process the data
 */
class GameViewModel : ViewModel() {
    private val _score = MutableLiveData(0)
    private val _currentWordCount = MutableLiveData(0)
    private val _currentScrambledWord = MutableLiveData<String>()

    // the getter of the private properties
    val currentWordCount: LiveData<Int> get() = _currentWordCount
    val score: LiveData<Int> get() = _score
    val currentScrambledWord: LiveData<String> get() = _currentScrambledWord


    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    private var wordList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private fun getNextWord() {
        currentWord = allWordsList.random()
        val temp = currentWord.toCharArray()
        temp.shuffle()
        //to ensure that the new word not to previous
        while (String(temp).equals(currentWord, false)) {
            temp.shuffle()
        }
        if (wordList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(temp)
            _currentWordCount.value=(_currentWordCount.value)?.inc()
            wordList.add(currentWord)
        }

    }

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWord()
    }
}