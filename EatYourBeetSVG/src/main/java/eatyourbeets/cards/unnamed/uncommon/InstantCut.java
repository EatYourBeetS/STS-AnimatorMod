package eatyourbeets.cards.unnamed.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnCardReshuffledSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class InstantCut extends UnnamedCard implements OnCardReshuffledSubscriber
{
    public static final EYBCardData DATA = Register(InstantCut.class)
            .SetAttack(1, CardRarity.UNCOMMON);

    public InstantCut()
    {
        super(DATA);

        Initialize(7, 0, 6, 7);
        SetUpgrade(3, 0, 0, 3);
    }

    @Override
    public void OnCardReshuffled(AbstractCard card, CardGroup sourcePile)
    {
        if (card == this && sourcePile == player.hand)
        {
            GameEffects.Queue.ShowCopy(this, Settings.WIDTH * 0.35f, Settings.HEIGHT * 0.5f);
            GameActions.Bottom.DealDamageToAll(DamageInfo.createDamageMatrix(secondaryValue, true), DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL)
            .SetVFX(true, false)
            .SetSoundPitch(1.3f, 1.4f)
            .SetDuration(0.1f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL)
        .SetVFX(true, false).SetVFXColor(Color.RED)
        .SetSoundPitch(1.3f, 1.4f)
        .SetDuration(0.1f, true);
        GameActions.Bottom.StackAmplification(p, m, magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onCardReshuffled.Subscribe(this);
    }
}