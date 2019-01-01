import React, { Component } from 'react';
import Navigation from './Components/Navigation';
import { UserProvider } from './Context/UserContext';
import './App.css';

class App extends Component {

  checkForErrors = (response) => {

    console.log('checking for errors...')

    console.log(response.ok)
    
    if (!response.ok) {
      console.log('error thrown!')
      this.loginError(); 
    } 

    return response; 
  }

  handleUserLogin = (username, password) => {
    fetch(`/api/login?username=${username}&password=${password}`,
      { method: 'post' })
      .then(this.checkForErrors).then(response => response.text()).then(text => console.log(text))
      .catch(error => this.loginError()); 
  }

  loginSuccess = () => {
    //TODO: Write me! 
  }

  loginError = () => {
    this.setState((state) => {
      let errors = state.errors; 

      errors['loginError'] = true; 

      return {errors: errors}; 

    }); 
  }

  state = {
    token: null,
    errors: {
      loginError: false, 
    }
  }

  render() {
    return (
      <div className="App">
        <UserProvider value={this.state}>
          <Navigation
            handleUserLogin={this.handleUserLogin}
          />
        </UserProvider>
      </div>
    );
  }
}

export default App;