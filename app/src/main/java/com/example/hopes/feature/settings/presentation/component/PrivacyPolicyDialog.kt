package com.example.hopes.feature.settings.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.hopes.R

/** 조항 하나(제목 + 본문)를 나타낸다. */
private data class PrivacyPolicyArticle(
    val heading: String,
    val body: String,
)

// 실제로 앱이 수집하는 항목(이메일·이름·비밀번호·학과·기수, 채팅 내용) 기준으로 작성했다.
// 담당자 연락처는 배포 전 실제 값으로 교체해야 한다.
private val privacyPolicyArticles = listOf(
    PrivacyPolicyArticle(
        heading = "제1조 수집하는 개인정보 항목",
        body = "hopes는 회원가입과 서비스 제공을 위해 다음의 정보를 수집합니다.\n\n" +
            "· 필수 항목: 학교 이메일(@gsm.hs.kr), 이름, 비밀번호, 기수\n" +
            "· 선택 항목: 학과(소프트웨어개발과 / IoT과 / AI과)\n" +
            "· 서비스 이용 중 생성되는 정보: 질문·답변 등 채팅 내용, 계정 생성일·활동 이력\n\n" +
            "hopes는 광고, 분석(애널리틱스), 크래시 리포팅 등 어떠한 외부 추적 도구도 사용하지 않으며, " +
            "위치정보·카메라·저장소 등 민감한 기기 권한을 요구하지 않습니다. 비밀번호는 서버에 암호화되어 " +
            "저장되며, 회사를 포함한 누구도 원문을 열람할 수 없습니다.",
    ),
    PrivacyPolicyArticle(
        heading = "제2조 개인정보의 수집 방법",
        body = "개인정보는 이용자가 앱에서 직접 입력하는 방식으로만 수집됩니다. 회원가입 시 학교 이메일로 " +
            "발송되는 인증코드를 확인해야 하며, 이는 실제 재학생·졸업생만 가입할 수 있도록 본인 확인 목적으로 " +
            "사용됩니다.",
    ),
    PrivacyPolicyArticle(
        heading = "제3조 개인정보의 수집·이용 목적",
        body = "· 회원 가입 의사 확인 및 본인(재학생·졸업생) 확인\n" +
            "· 로그인 등 회원제 서비스 제공에 따른 본인 식별·인증\n" +
            "· 질문·답변 기능 등 서비스 핵심 기능 제공\n" +
            "· 학과·기수 정보를 활용한 질문 대상(선후배) 매칭 참고\n" +
            "· 부정 이용 방지 및 서비스 문의 대응",
    ),
    PrivacyPolicyArticle(
        heading = "제4조 개인정보의 보유·이용 기간",
        body = "원칙적으로 개인정보 수집·이용 목적이 달성되면 지체 없이 파기합니다. 이용자가 [설정 > 회원탈퇴] " +
            "절차로 탈퇴를 요청하면 계정과 연결된 개인정보를 지체 없이 삭제하며, 관계 법령이 일정 기간 보존을 " +
            "요구하는 경우에는 그 기간 동안 보관 후 파기합니다.",
    ),
    PrivacyPolicyArticle(
        heading = "제5조 개인정보의 제3자 제공 및 위탁",
        body = "hopes는 이용자의 개인정보를 원칙적으로 외부에 제공하지 않습니다. 현재 개인정보 처리를 위탁하는 " +
            "외부 업체는 없으며, 광고·분석 목적의 제3자 SDK도 사용하지 않습니다. 향후 위탁이 발생할 경우 위탁받는 " +
            "자와 위탁 업무 내용을 이 방침을 통해 사전에 고지하겠습니다.",
    ),
    PrivacyPolicyArticle(
        heading = "제6조 이용자의 권리와 행사 방법",
        body = "이용자는 언제든지 본인의 개인정보 열람·정정을 요청하거나, [설정 > 회원탈퇴]를 통해 개인정보 삭제를 " +
            "요청할 수 있습니다. 제9조의 연락처로 문의하시면 관계 법령에 따라 지체 없이 조치합니다.",
    ),
    PrivacyPolicyArticle(
        heading = "제7조 개인정보의 안전성 확보 조치",
        body = "· 비밀번호 등 주요 정보의 암호화 저장\n" +
            "· 회원 인증 절차를 통한 비인가 접근 통제\n" +
            "· 최소한의 접근 권한(인터넷)만 사용하는 설계 — 위치·카메라·연락처 등 민감 권한을 요구하지 않음",
    ),
    PrivacyPolicyArticle(
        heading = "제8조 만 14세 미만 아동 및 미성년자",
        body = "hopes는 회원가입 시 광주소프트웨어마이스터고등학교 이메일(@gsm.hs.kr) 인증을 요구하며, 서비스 " +
            "특성상 주 이용자는 고등학생입니다. 만 14세 미만 아동의 개인정보를 별도로 수집하도록 설계되어 있지 " +
            "않으며, 법정대리인의 동의 없이 만 14세 미만 아동의 개인정보가 수집된 사실을 인지하는 경우 지체 없이 " +
            "파기합니다.",
    ),
    PrivacyPolicyArticle(
        heading = "제9조 개인정보 보호책임자",
        body = "개인정보 처리에 관한 문의, 불만 처리, 피해 구제 등을 위해 담당자를 지정하고 있습니다.\n\n" +
            "담당자: [담당자 이름]\n" +
            "이메일: [문의받을 이메일 주소]\n\n" +
            "위 담당자 정보는 배포 전 실제 연락처로 교체해 주세요.",
    ),
    PrivacyPolicyArticle(
        heading = "제10조 고지의 의무",
        body = "이 개인정보처리방침의 내용이 추가·삭제 및 수정이 있을 경우, 시행 최소 7일 전부터 앱 내 공지사항 " +
            "또는 이 화면을 통해 고지합니다.\n\n시행일: 2026.09.08",
    ),
)

/**
 * 개인정보처리방침을 앱 내 팝업으로 보여준다. 실제 코드에서 수집하는 항목 기준으로 작성된
 * 고정 문서라 WebView 없이 네이티브 Compose Text로 렌더링한다.
 */
@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(vertical = 48.dp, horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 12.dp, top = 16.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.privacy_policy),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                ) {
                    privacyPolicyArticles.forEachIndexed { index, article ->
                        Text(
                            text = article.heading,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = article.body,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        if (index != privacyPolicyArticles.lastIndex) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}
