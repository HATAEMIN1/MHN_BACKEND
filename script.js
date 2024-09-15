import http from 'k6/http';
import { sleep, check } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');

export const options = {
    stages: [
        { duration: '30s', target: 50 },   // 30초 동안 50명으로 증가
        { duration: '30s', target: 100 },   // 1분 동안 100명으로 증가
        { duration: '30s', target: 200 },   // 2분 동안 200명으로 증가
        { duration: '30s', target: 100 },   // 1분 동안 100명으로 감소
        { duration: '30s', target: 0 },    // 30초 동안 0명으로 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% of requests should be below 500ms
        errors: ['rate<0.1'],  // 에러율 10% 미만
    },
};

const BASE_URL = 'http://localhost:8080/api/v1';
const MIN_MEMBER_ID = 42;
const MAX_MEMBER_ID = 71;
export default function () {
    const memberId = Math.floor(Math.random() * (MAX_MEMBER_ID - MIN_MEMBER_ID + 1)) + MIN_MEMBER_ID;
    // const memberId = 1
    const page = Math.floor(Math.random() * 4);  // 0부터 4까지 랜덤 페이지
    const size = 4;  // 페이지 크기

    const response = http.get(`${BASE_URL}/charts?memberId=${memberId}&page=${page}&size=${size}`);

    check(response, {
        'status is 200': (r) => r.status === 200,
        'response time < 500ms': (r) => r.timings.duration < 500,
    }) || errorRate.add(1);

    sleep(1);
}
