@import hexlet.code.dto.url.UrlsPage
@import hexlet.code.util.HtmlUtils
@param UrlsPage page
!{var urlChecks = page.getLastChecks();}

@template.layout.page(
page = page,
content = @`
    @if(!page.getUrls().isEmpty())
        <div class="container-lg mt-5">
            <h1>Сайты</h1>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th class="col-1">Имя</th>
                        <th class="col-2">Последняя проверка</th>
                        <th class="col-1">Код ответа</th>
                    </tr>
                </thead>
                <tbody>
                @for(var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                        @if(!urlChecks.isEmpty())
                            !{var urlCheck = urlChecks.get(url.getId());}
                            @if(urlCheck != null)
                                <td>${HtmlUtils.getTimestampFormatted(urlCheck.getCreatedAt())}</td>
                                <td>
                                    ${!urlCheck.getStatusCode().equals(0)
                                        ? String.valueOf(urlCheck.getStatusCode())
                                        : ""}
                                </td>
                            @else
                                <td></td>
                                <td></td>
                            @endif
                        @else
                            <td></td>
                            <td></td>
                        @endif
                    </tr>
                @endfor
                <%--@for(var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                        <td>${url.getLastTimeCheck() != null ? HtmlUtils.getTimestampFormatted(url.getLastTimeCheck()) : ""}</td>
                        <td>${!url.getStatusCode().equals(0) ? String.valueOf(url.getStatusCode()) : ""}</td>
                    </tr>
                @endfor--%>
                </tbody>
            </table>
        </div>
    @else
        <p>Список пуст</p>
    @endif
`)
