// import http from 'k6/http';
// import { sleep } from 'k6';
//
// export default function () {
//     const memberId = 1; // 테스트용 memberId, 실제 존재하는 ID로 변경 필요
//     const page = Math.floor(Math.random() * 5); // 0부터 4까지 랜덤 페이지
//     const size = 4; // 페이지 크기
//
//     const response = http.get(`http://localhost:8080/api/v1/charts?memberId=${memberId}&page=${page}&size=${size}`);
//
//     console.log(`Response status: ${response.status}`);
//     console.log(`Response body: ${response.body}`);
//
//     sleep(1);
// }

import http from 'k6/http';
import { sleep, check } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

export const options = {
    stages: [
        { duration: '1m', target: 10 },  // 1분 동안 10명의 가상 사용자로 증가
        { duration: '3m', target: 50 },  // 3분 동안 50명으로 증가
        { duration: '2m', target: 50 },  // 2분 동안 50명 유지
        { duration: '1m', target: 0 },   // 1분 동안 0명으로 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% of requests should be below 500ms
        errors: ['rate<0.1'],  // 에러율 10% 미만
    },
};

const BASE_URL = 'http://localhost:8080/api/v1';

export default function () {
    // const memberId = Math.floor(Math.random() * 100) + 1;  // 1부터 100까지 랜덤 memberId
    const memberId = 1
    const page = Math.floor(Math.random() * 4);  // 0부터 4까지 랜덤 페이지
    const size = 4;  // 페이지 크기

    const response = http.get(`${BASE_URL}/charts?memberId=${memberId}&page=${page}&size=${size}`);

    check(response, {
        'status is 200': (r) => r.status === 200,
        'response time < 500ms': (r) => r.timings.duration < 500,
    }) || errorRate.add(1);

    sleep(1);
}