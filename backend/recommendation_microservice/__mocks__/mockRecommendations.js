'use strict';
//let's try this shit:
//looks promising
import { callAnotherFunction } from '../../../utils';

callAnotherFunction.mockImplementation(() => Promise.resolve('someValue'));

test('test' , done => {
  const testData = {
    body: {
      emailAddress: 'email',
      emailType: 'type'
    }
  };

  function callback(dataTest123) {
    expect(dataTest123).toBe(success: true, returnValue: 'someValue');
    done();
  }

  handler(testData, null, callback);
});

import testData from '../data.js';
import { handler } from '../src/index.js';
import * as Utils from '../../../utils'


jest.mock('../../../utils');
beforeAll(() => {
  Utils.callAnotherLambdaFunction = jest.fn().mockResolvedValue('test');
});

describe('>>> SEND EMAIL LAMBDA', () => {
  it('should return a good value', async () => {
    const callback = jest.fn()
    await handler(testData, null, callback);
    expect(callback).toBeCalledWith(null, {success: true, returnValue: 'test'})
  });
})

import { recommendationController } from './recommendationController.js'

recommendationController.mockImplementation(getSuggestedPosts);

test('test mocking getSuggestedPosts', done => {
    const te
})
jest.mock('/suggestedPosts/request/:item');



// --------- j's attempt


// export function getUserName(userID) {
//     return request(`/users/${userID}`).then(user => user.name);
// }
  

const http = require('http');

export default function request(url) {
  return new Promise(resolve => {
    // This is an example of an http request, for example to fetch
    // user data from an API.
    // This module is being mocked in __mocks__/request.js
    http.get({path: url}, response => {
      let data = '';
      response.on('data', _data => (data += _data));
      response.on('end', () => resolve(data));
    });
  });
}

