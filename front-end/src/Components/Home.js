import React from 'react'; 
import { Jumbotron } from 'reactstrap'; 

export default class Home extends React.Component {

    render() {
        return(
            <div className="text-left"> 
                <Jumbotron> 
                    <h1 className="display-3">Welcome to Q</h1>
                    <hr className="my-2" />
                    <p>Find a Queue:</p>
                </Jumbotron>
            </div>
        ); 
    }
}