package eatyourbeets.cards.animator.auras;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Aura1 extends Aura
{
    public static final EYBCardData DATA = RegisterAura(Aura1.class);
    public static final float D_X = CardGroup.DRAW_PILE_X * 1.5f;
    public static final float D_Y = CardGroup.DRAW_PILE_Y * 3.5f;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Aura1()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
        }

        choices.Select(1, m);

        GameActions.Bottom.SelectFromPile(name, 1, p.drawPile)
        .SetOptions(true, true)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.Motivate(cards.get(0), magicNumber)
                .AddCallback(c -> GameEffects.List.ShowCardBriefly(c.makeStatEquivalentCopy(), D_X, D_Y));
            }
        });
    }

    @Override
    public boolean CanActivate(int currentTurn)
    {
        return super.CanActivate(upgraded ? (currentTurn + 1) : currentTurn);
    }
}