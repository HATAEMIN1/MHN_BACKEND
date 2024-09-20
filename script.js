import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export const options = {
    stages: [
        { duration: '30s', target: 200 },  // 빠르게 200 VUs까지 증가
        { duration: '1m', target: 200 },   // 1분 동안 200 VUs 유지
        { duration: '30s', target: 400 },  // 30초 동안 400 VUs로 증가
        { duration: '1m', target: 400 },   // 1분 동안 400 VUs 유지
        { duration: '30s', target: 0 },    // 30초 동안 0으로 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% of requests should be below 500ms
        http_reqs: ['rate>1000'],         // 목표: 초당 1000개 이상의 요청 처리
    },
};

const BASE_URL = 'http://localhost:8080/api/v1';
const MIN_MEMBER_ID = 134;
const MAX_MEMBER_ID = 163;

const errorRate = new Rate('errors');

export default function () {
    const memberId = Math.floor(Math.random() * (MAX_MEMBER_ID - MIN_MEMBER_ID + 1)) + MIN_MEMBER_ID;
    const page = Math.floor(Math.random() * 4);
    const size = 4;

    const response = http.get(`${BASE_URL}/charts?memberId=${memberId}&page=${page}&size=${size}`);

    check(response, {
        'status is 200': (r) => r.status === 200,
    }) || errorRate.add(1);

    // 대기 시간을 0.1초로 줄임
    sleep(0.1);
}