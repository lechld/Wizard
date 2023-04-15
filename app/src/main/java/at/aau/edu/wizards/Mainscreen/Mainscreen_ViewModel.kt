package at.aau.edu.wizards.Mainscreen

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class Mainscreen_ViewModel(
    private val someDependency: String, // can be anything, whatever we need
    private val dataSource: Mainscreen_DataSource, // usually ViewModel will receive some Repository where it reads (sometimes writes( data from
) : ViewModel() /* Need to extend ViewModel */ {

    // keep mutable property private
    private val _dataToBeObservedInFragment = MutableLiveData<List<String>>()

    // expose (having public) non-changeable LiveData
    val dataToBeObservedInFragment: LiveData<List<String>> = _dataToBeObservedInFragment

    init {
        // that launch allows us to call a function with "suspend"
        viewModelScope.launch {
            val receivedData = dataSource.provideData(someDependency)

            _dataToBeObservedInFragment.postValue(receivedData) // update the observable value. Fragment will receive that value if it observes
        }
    }

    // Every ViewModel needs to be created by some Factory
    // There are multiple ways (some use one class which creates all
    // viewModels across the app. I prefer to write a little boilerplate code
    // and create a single factory for each viewmodel
    class Factory(
        private val input: String,
        private val sampleDataSource: Mainscreen_DataSource,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(Mainscreen_ViewModel::class.java)) {
                // creates the instance and provides someDependency
                Mainscreen_ViewModel(input, sampleDataSource) as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}