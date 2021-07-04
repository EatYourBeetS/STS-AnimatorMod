package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.animator.status.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.vfx.HemokinesisEffect;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class Charlotte extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Charlotte.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal);

    public Charlotte()
    {
        super(DATA);

        Initialize(60, 0, 3);
        SetUpgrade(20, 0, 0);
        SetScaling(3, 0, 6);

        SetSynergy(Synergies.MadokaMagica);
        SetAlignment(2, 0, 2, 0, 2);
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
            GameEffects.List.Add(new BiteEffect(e.hb.cX, e.hb.cY - 40.0F * Settings.scale, Color.WHITE.cpy()));
            GameEffects.List.Add(new HemokinesisEffect(e.hb.cX, e.hb.cY, player.hb.cX, player.hb.cY));
            GameEffects.List.Add(new BorderFlashEffect(Color.RED));
            GameActions.Top.Add(new ShakeScreenAction(0.3f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        });
        GameActions.Bottom.StackPower(TargetHelper.AllCharacters(), PowerHelper.Vulnerable, magicNumber);
    }
}