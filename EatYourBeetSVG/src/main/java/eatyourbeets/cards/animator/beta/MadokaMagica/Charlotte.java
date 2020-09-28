package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.vfx.HemokinesisEffect;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Charlotte extends AnimatorCard implements OnAfterCardPlayedSubscriber
{
    public static final EYBCardData DATA = Register(Charlotte.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal);

    boolean lastCardWasGriefSeed = false;

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
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAfterCardPlayed.Subscribe(this);
    }


    @Override
    protected float GetInitialDamage()
    {
        return lastCardWasGriefSeed ? baseDamage * 4 : baseDamage;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (IntellectStance.IsActive())
        {
            return super.GetDamageInfo().AddMultiplier(2);
        }
        else
        {
            return super.GetDamageInfo();
        }
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (card.cardID.equals(Curse_GriefSeed.DATA.ID))
        {
            lastCardWasGriefSeed = true;
        }
        else
        {
            lastCardWasGriefSeed = false;
        }
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