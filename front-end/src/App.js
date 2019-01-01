import React, { Component } from 'react';
import Navigation from './Components/Navigation';
import { UserProvider } from './Context/UserContext';
import './App.css';
import * as jwt_decode from 'jwt-decode';
import { BrowserRouter, Route } from 'react-router-dom';
import Home from './Components/Home'
import { ErrorProvider } from './Context/ErrorContext';

class App extends Component {

  checkForErrors = (response) => {

    if (!response.ok) {
      throw new Error('Invalid Username or Password');
    }

    return response;
  }

  handleUserLogin = (username, password) => {
    fetch(`/api/login?username=${username}&password=${password}`,
      { method: 'post' })
      .then(this.checkForErrors)
      .then((response) => response.json())
      .then((json) => {
        let user = jwt_decode(json.token);
        this.loginSuccess(user, json.token);
      })
      .catch((error) => {
        this.loginError();
      });
  }

  handleUserLogout = () => {
    this.setState((state) => ({
      user: {
        user:null, 
        token:null
      }
    }));
  }

  loginSuccess = (user, token) => {
    this.setState((state) => {

      // Clear out login error if it is flagged. 
      let errors = state.errors;
      errors['loginError'] = false;

      return ({
        errors: errors,
        user: {
          user: user, 
          token: token
        }
      });

    });
  }

  loginError = () => {
    this.setState((state) => {
      let errors = state.errors;

      errors['loginError'] = true;

      return { errors: errors };

    });
  }

  state = {
    token: null,
    errors: {
      loginError: false,
    }, 
    user: {
      user: null, 
      token: null, 
    }
  }

  render() {
    return (
      <BrowserRouter>
        <div className="App">
          <ErrorProvider value={this.state.errors}>
          <UserProvider value={this.state.user}>
            <Navigation
              handleUserLogin={this.handleUserLogin}
              handleUserLogout={this.handleUserLogout}
              user={this.state.user}
            />
            <Route exact path="/" component={ Home }/>
          </UserProvider>
          </ErrorProvider>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;