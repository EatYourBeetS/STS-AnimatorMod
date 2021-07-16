package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.status.SearingBurn;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HuTao extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HuTao.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing).SetSeriesFromClassPackage();

    static
    {
        DATA.AddPreview(new SearingBurn(), false);
    }

    public HuTao()
    {
        super(DATA);

        Initialize(2, 0, 3, 2);
        SetUpgrade(1, 0, 1, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(2, 0, 4);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.MakeCardInHand(new SearingBurn())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        GameActions.Bottom.SelectFromPile(name, magicNumber, p.drawPile, p.hand, p.discardPile)
                .SetOptions(true, true)
                .SetFilter(GameUtilities::IsCurseOrStatus)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        GameActions.Bottom.Exhaust(card)
                                .ShowEffect(true, true)
                                .AddCallback(() -> GameActions.Bottom.MakeCardInDrawPile(new SearingBurn()));
                    }
                });

    }
}