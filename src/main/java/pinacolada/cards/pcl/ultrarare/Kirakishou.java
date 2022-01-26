package pinacolada.cards.pcl.ultrarare;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.RotatingList;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.powers.special.MindControlPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Kirakishou extends PCLCard_UltraRare
{
    public static PCLCardTooltip MindControlInfo;
    public static final PCLCardData DATA
    	= Register(Kirakishou.class)
    	.SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.Normal)
    	.SetColor(CardColor.COLORLESS).SetSeries(CardSeries.RozenMaiden)
        .PostInitialize(data ->
            {
                PowerStrings mcp = GR.GetPowerStrings(MindControlPower.POWER_ID);
                MindControlInfo = new PCLCardTooltip(mcp.NAME, Kirakishou.DATA.Strings.EXTENDED_DESCRIPTION[0]);
            });


    public Kirakishou()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Star(1);
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
            PCLActions.Bottom.ApplyConstricted(TargetHelper.Enemies(), secondaryValue);
        }
        PCLActions.Last.Purge(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.drawPile.size() < secondaryValue) {
            PCLActions.Top.Callback(new EmptyDeckShuffleAction(), () -> DoAction(p, m));
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

        PCLActions.Bottom.SelectFromPile(name, magicNumber, group)
                .SetOptions(upgraded ? null : CardSelection.Top, false).AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        p.drawPile.removeCard(c);

                        PCLGameEffects.List.ShowCardBriefly(c.makeStatEquivalentCopy());
                        PCLActions.Bottom.ApplyPower(new MindControlPower(m, c)).AllowDuplicates(true);
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
