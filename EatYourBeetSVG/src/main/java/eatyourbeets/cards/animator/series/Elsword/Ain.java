package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Ain extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ain.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Ain()
    {
        super(DATA);

        Initialize(3, 0, 2, 1);
        SetUpgrade(0, 0, 1, 0);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.Elsword);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainIntellect(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //GameActions.Bottom.VFX(new BlizzardEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.6f);
        GameActions.Bottom.Callback(() ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + magicNumber + 5;

            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f - (float)frostCount / 200f);
            for (int i = 0; i < frostCount; i++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE).SetVFX(false, true);
        }

        GameActions.Bottom.ChannelRandomOrb(true);

        if (HasSynergy())
        {
            GameActions.Bottom.GainIntellect(secondaryValue);
        }
    }
}