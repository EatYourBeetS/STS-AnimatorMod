package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class ShioriSekine extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShioriSekine.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ShioriSekine()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Light(1, 1, 0);
        SetEthereal(true);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return CombatStats.CardsExhaustedThisTurn() > 0;
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Bottom.GainWillpower(1, upgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.PlayFromPile(name, magicNumber, m, p.exhaustPile).SetOptions(true, false).SetFilter(AfterLifeMod::IsAdded).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                GameActions.Last.Purge(c);
            }
        });
    }
}