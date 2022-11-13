package eatyourbeets.cards.animatorClassic.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.animatorClassic.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class NagisaMomoe_CharlotteAlt extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(NagisaMomoe_CharlotteAlt.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal);

    public NagisaMomoe_CharlotteAlt()
    {
        super(DATA);

        Initialize(60, 0, 3);
        SetUpgrade(20, 0, 0);
        SetScaling(3, 0, 6);

        this.series = CardSeries.MadokaMagica;
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(e ->
        {
            GameEffects.List.BorderFlash(Color.RED);
            return GameEffects.List.Add(VFX.Hemokinesis(player.hb, e.hb)).duration * 0.1f;
        });
        GameActions.Bottom.StackPower(TargetHelper.AllCharacters(), PowerHelper.Vulnerable, magicNumber);
    }
}