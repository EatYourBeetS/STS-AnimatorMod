package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_NextTurnBlock;
import eatyourbeets.misc.GenericEffects.GenericEffect_NextTurnDraw;
import eatyourbeets.misc.GenericEffects.GenericEffect_Scry;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameUtilities;

public class OrikoMikuni extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrikoMikuni.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public OrikoMikuni()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, 1, 2);

        SetSeries(CardSeries.MadokaMagica);
        SetAffinity(0, 0, 1, 2, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_Scry(magicNumber));
        choices.AddEffect(new GenericEffect_NextTurnDraw(1));
        choices.AddEffect(new GenericEffect_NextTurnBlock(secondaryValue));

        if (GameUtilities.InStance(IntellectStance.STANCE_ID) && CombatStats.TryActivateLimited(cardID))
        {
            choices.Select(3, m);
        }
        else
        {
            choices.Select(1, m);
        }
    }
}