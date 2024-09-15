import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export const options = {
    stages: [
        { duration: '30s', target: 10 },  // 10명의 동시 사용자로 ramping up
        { duration: '1m', target: 10 },   // 1분 동안 10명 유지
        { duration: '30s', target: 20 },  // 20명으로 증가
        { duration: '1m', target: 20 },   // 1분 동안 20명 유지
        { duration: '30s', target: 0 },   // 0명으로 ramping down
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95%의 요청이 500ms 이내에 완료되어야 함
        'http_req_duration{chartType:single}': ['p(95)<100'], // 단일 차트 조회
        'http_req_duration{chartType:list}': ['p(95)<200'],   // 차트 목록 조회
    },
};

const BASE_URL = 'http://localhost:8080/api/v1';
const MIN_CHART_ID = 18;
const MAX_CHART_ID = 117;

const errorRate = new Rate('errors');

export default function () {
    // 단일 차트 조회
    let chartId = Math.floor(Math.random() * (MAX_CHART_ID - MIN_CHART_ID + 1)) + MIN_CHART_ID;
    let res = http.get(`${BASE_URL}/charts/${chartId}`, {
        tags: { chartType: 'single' }
    });

    check(res, {
        'single chart status is 200': (r) => r.status === 200,
    }) || errorRate.add(1);

    sleep(1);

    // 차트 목록 조회 (페이지당 10개, 랜덤 페이지)
    let page = Math.floor(Math.random() * 10);
    res = http.get(`${BASE_URL}/charts?page=${page}&size=10`, {
        tags: { chartType: 'list' }
    });

    check(res, {
        'chart list status is 200': (r) => r.status === 200,
    }) || errorRate.add(1);

    sleep(1);
}