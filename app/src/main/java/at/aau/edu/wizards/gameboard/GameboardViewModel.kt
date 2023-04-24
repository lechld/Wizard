package at.aau.edu.wizards.gameboard

import androidx.lifecycle.*
import at.aau.edu.wizards.R
import at.aau.edu.wizards.sample.SampleDataSource
import at.aau.edu.wizards.sample.SampleViewModel
import kotlinx.coroutines.launch

        //AUFBAUE VIEWMODEL
class GameboardViewModel(
    private val cards: Cards
) : ViewModel() {

    private val _dataToBeObservedInFragment = MutableLiveData<List<String>>()

    val dataToBeObservedInFragment: LiveData<List<String>> = _dataToBeObservedInFragment

    init {
        viewModelScope.launch {
            val receivedData = dataSource.provideData(someDependency)

            _dataToBeObservedInFragment.postValue(receivedData)
        }
    }

    class Factory(
        private val input: String,
        private val cards: Cards,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SampleViewModel::class.java)) {
                // creates the instance and provides someDependency
                SampleViewModel(input, Cards) as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }

    /*
    private fun dataInitialize() {

        cardsArrayList = arrayListOf<Cards>()

        imageId = arrayOf(
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card,
            R.drawable.card
        )


        for (i in imageId.indices){
            val news = Cards(imageId[i])
            cardsArrayList.add(news)
        }
    }

     */
}


