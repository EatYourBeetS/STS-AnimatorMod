package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.Hemokinesis2Effect;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.ArrayList;

public class IchigoBankai extends AnimatorCard implements MartialArtist, Hidden
{
    public static final String ID = Register(IchigoBankai.class.getSimpleName());

    public IchigoBankai()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);

        Initialize(11, 0, 2);

        SetExhaust(true);
        SetMultiDamage(true);
        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        MartialArtist.ApplyScaling(this, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75F);
        GameActionsHelper.DamageAllEnemiesPiercing(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, true);
        GameActionsHelper.DrawCard(p, magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}

/*
	"animator:IchigoBankai":
	{
		"DESCRIPTION": "Deal !D! piercing damage to ALL enemies. Draw !M! cards. Exhaust.",
		"NAME": "Ichigo Bankai"
	},
	"animator:IchigoKurosaki":
	{
		"DESCRIPTION": "Next turn, gain X [E] and Temporary Strength. If X is at least !M! , Purge and obtain [#EFC851]Ichigo[] [#EFC851]Bankai[].",
		"NAME": "Ichigo Kurosaki"
	},
*/