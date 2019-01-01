import React from 'react'; 

const ErrorContext = React.createContext(); 

const ErrorProvider = ErrorContext.Provider; 
const ErrorConsumer = ErrorContext.Consumer; 

export {ErrorConsumer, ErrorProvider} 