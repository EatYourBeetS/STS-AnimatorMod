package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.VFX;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class Charlotte extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Charlotte.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal)
            .SetSeries(CardSeries.MadokaMagica);

    public Charlotte()
    {
        super(DATA);

        Initialize(60, 0, 3);
        SetUpgrade(20, 0, 0);

        SetAffinity_Dark(2, 0, 3);
        SetAffinity_Red(1);
        SetAffinity_Blue(1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (!player.hasPower(ArtifactPower.POWER_ID))
        {
            for (EnemyIntent intent : GameUtilities.GetIntents())
            {
                intent.AddPlayerVulnerable();
            }
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);

        if (playable)
        {
            if (JUtils.Find(AbstractDungeon.actionManager.cardsPlayedThisTurn, Curse_GriefSeed.class::isInstance) == null)
            {
                cantUseMessage = cardData.Strings.EXTENDED_DESCRIPTION[0];
                return false;
            }
        }

        return playable;
    }
    
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(e ->
        {
            float wait = GameEffects.List.Add(VFX.Bite(e.hb)).duration;
            GameEffects.List.Add(VFX.Hemokinesis(player.hb, e.hb));
            GameEffects.List.BorderFlash(Color.RED);
            GameActions.Top.ShakeScreen(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);
            return wait;
        });
        GameActions.Bottom.StackPower(TargetHelper.AllCharacters(), PowerHelper.Vulnerable, magicNumber);
    }
}