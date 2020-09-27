package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.vfx.HemokinesisEffect;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Charlotte extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Charlotte.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal);

    public Charlotte()
    {
        super(DATA);

        Initialize(8, 0, 4);
        SetUpgrade(0, 0, 4);
        SetScaling(1, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
        SetSpellcaster();
    }

    @Override
    protected float GetInitialDamage()
    {
        int damage = baseDamage;

        damage *= (float) Math.pow(2, player.hand.getCardsOfType(CardType.CURSE).size());

        return damage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int numberAttacks = 1;

        if (GameUtilities.InStance(IntellectStance.STANCE_ID))
        {
            numberAttacks = 2;
        }

        for (int i=0; i<numberAttacks; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(e ->
            {
                GameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40.0F * Settings.scale, Color.WHITE.cpy()));
                if (damage > 30)
                {
                    GameEffects.List.Add(new HemokinesisEffect(e.hb.cX, e.hb.cY, player.hb.cX, player.hb.cY));
                    GameEffects.List.Add(new BorderFlashEffect(Color.RED));
                    GameActions.Top.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
                }
            })
            .AddCallback(c ->
            {
                if (GameUtilities.TriggerOnKill(c, true))
                {
                    GameActions.Bottom.Heal(magicNumber);
                }
            });
        }
    }
}