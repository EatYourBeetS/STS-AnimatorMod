package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Gluttony extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gluttony.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int MINIMUM_CARDS = 4;

    public Gluttony()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);

        SetAffinity_Red(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        int xValue = GetXValue();
        return xValue > 0 ? TempHPAttribute.Instance.SetCard(this).SetText(GetXValue(), Settings.CREAM_COLOR) : null;
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(MINIMUM_CARDS);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(player.hand.size() < MINIMUM_CARDS);
    }

    @Override
    public int GetXValue() {
        return player != null ? (magicNumber * (player.hand.size() - (player.hand.contains(this) ? 1 : 0))) : -1;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        if (p.hand.size() >= MINIMUM_CARDS)
        {
            for (AbstractCard c : player.hand.group) {
                EYBCard eCard = JUtils.SafeCast(c, EYBCard.class);
                if (eCard != null && eCard.affinities != null) {
                    CombatStats.Affinities.AddAffinities(eCard.affinities);
                }
                GameActions.Bottom.GainTemporaryHP(magicNumber);
            }
            GameActions.Delayed.ModifyAffinityLevel(player.hand, 9999, Affinity.General,  -1, true).AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    EYBCard eCard = JUtils.SafeCast(c, EYBCard.class);
                    if (eCard == null || eCard.affinities.GetLevel(Affinity.General) <= 0) {
                        GameActions.Last.Exhaust(c).AddCallback(() -> GameActions.Bottom.GainMight(secondaryValue));
                    }
                }
            });
        }
    }
}