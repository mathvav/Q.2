import React from 'react';
import { Form, FormGroup, Input, Button, Alert } from 'reactstrap';

export default class LoginForm extends React.Component {

    username = React.createRef();
    password = React.createRef();

    handleSubmit = (event) => {
        event.preventDefault();

        let username = this.username.current.value;
        let password = this.password.current.value;

        this.props.handleUserLogin(username, password)
    }

    loginErrorAlert = () => {
        if (this.props.loginError) {
            return (
                <Alert color="danger" className="mx-2">Invalid Username or Password!</Alert>
            );
        } else {
            return (null)
        }
    }

    render() {
        return (
            <div>
                {this.loginErrorAlert()}
                <Form className="px-4" onSubmit={this.handleSubmit}>
                    <FormGroup row>
                        <Input type="username" name="email" id="email" placeholder="username" innerRef={this.username} />
                    </FormGroup>
                    <FormGroup row>
                        <Input type="password" name="password" id="password" placeholder="password" innerRef={this.password} />
                    </FormGroup>
                    <FormGroup row>
                        <Button color="primary" className="mr-2">
                            Login
                        </Button>
                        <Button color="secondary">
                            New User
                        </Button>
                    </FormGroup>
                </Form>
            </div>
        );
    }
}