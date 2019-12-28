package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Ain extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Ain.class, EYBCardBadge.Synergy, EYBCardBadge.Discard);

    public Ain()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(3, 0, 2, 1);
        SetUpgrade(0, 0, 1, 0);

        SetMultiDamage(true);
        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainIntellect(secondaryValue);
        }
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + Spellcaster.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //GameActions.Bottom.VFX(new BlizzardEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.6f);
        GameActions.Bottom.Callback(__ ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + magicNumber + 5;

            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25F - (float)frostCount / 200.0F);
            for (int i = 0; i < frostCount; i++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });

        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
            .SetOptions(true, false);
        }

        if (HasSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainIntellect(secondaryValue);
        }
    }
}