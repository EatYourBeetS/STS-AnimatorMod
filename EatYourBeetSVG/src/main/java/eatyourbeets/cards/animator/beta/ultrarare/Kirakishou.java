package eatyourbeets.cards.animator.beta.ultrarare;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.MindControlPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

public class Kirakishou extends AnimatorCard_UltraRare
{
    public static EYBCardTooltip MindControlInfo;
    public static final EYBCardData DATA
    	= Register(Kirakishou.class)
    	.SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.Normal)
    	.SetColor(CardColor.COLORLESS).SetSeries(CardSeries.RozenMaiden)
        .PostInitialize(data ->
            {
                PowerStrings mcp = GR.GetPowerStrings(MindControlPower.POWER_ID);
                MindControlInfo = new EYBCardTooltip(mcp.NAME, Kirakishou.DATA.Strings.EXTENDED_DESCRIPTION[0]);
            });


    public Kirakishou()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Star(2);
        SetPurge(true);

        SetDrawPileCardPreview(this::FindCards);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (tooltips != null && MindControlInfo != null && !tooltips.contains(MindControlInfo))
        {
            tooltips.add(MindControlInfo);
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && m != null && m.type != AbstractMonster.EnemyType.BOSS;
    }


    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        super.Render(sb, hovered, selected, library);

        if (!library)
        {
            this.drawPileCardPreview.Render(sb);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.ApplyConstricted(TargetHelper.Enemies(), secondaryValue);
        }
        GameActions.Last.Purge(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.drawPile.size() < secondaryValue) {
            GameActions.Top.Callback(new EmptyDeckShuffleAction(), () -> DoAction(p, m));
        }
        else {
            DoAction(p, m);
        }
    }

    private void DoAction(AbstractPlayer p, AbstractMonster m) {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (int i = 0; i < secondaryValue; i++) {
            int index = player.drawPile.group.size() - 1 - i;
            if (index >= 0) {
                group.addToTop(player.drawPile.group.get(index));
            }
        }

        GameActions.Bottom.SelectFromPile(name, magicNumber, group)
                .SetOptions(upgraded ? null : CardSelection.Top, false).AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        p.drawPile.removeCard(c);

                        GameEffects.List.ShowCardBriefly(c.makeStatEquivalentCopy());
                        GameActions.Bottom.ApplyPower(new MindControlPower(m, c)).AllowDuplicates(true);
                    }
                });
    }

    private void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        cards.Clear();
        for (int i = 0; i < secondaryValue; i++) {
            int index = player.drawPile.group.size() - 1 - i;
            if (index >= 0) {
                cards.Add(player.drawPile.group.get(index));
            }
        }
    }
}
