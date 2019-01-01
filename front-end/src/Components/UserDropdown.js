import React from 'react'; 
import { Button } from 'reactstrap';

export default class UserDropdown extends React.Component {

    render() {
        return(
            <div className="p-2">
                <h2>{this.props.user.sub}</h2>
                <Button color="danger" onClick={() => this.props.handleUserLogout()}>Log Out</Button>
            </div>
        );
    }

}