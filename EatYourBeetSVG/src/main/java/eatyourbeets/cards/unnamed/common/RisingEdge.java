package eatyourbeets.cards.unnamed.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.Mathf;

public class RisingEdge extends UnnamedCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(RisingEdge.class)
            .SetAttack(0, CardRarity.COMMON);

    public RisingEdge()
    {
        super(DATA);

        Initialize(2, 0, 7);
        SetUpgrade(0, 0, 2);

        SetPurge(true);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        GameActions.Bottom.MoveCard(this, CombatStats.PurgedCards, player.hand)
        .AddCallback(card ->
        {
            if (card != null)
            {
                CostModifiers.For(card).Add(1);
                DamageModifiers.For(card).Add(card.magicNumber);
                card.misc += 1;
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR)
        .SetVFXColor(Color.VIOLET)
        .SetVFXScale(Mathf.Min((misc + 1) * 0.5f, 2f))
        .SetSoundPitch(Mathf.Max(0.5f, 1.3f - (misc * 0.2f)));
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }
}